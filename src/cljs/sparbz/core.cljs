(ns sparbz.core
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [sparbz.constants :as const]
   [secretary.core :as secretary]
   [goog.events :as events]
   [goog.history.EventType :as EventType]
   [reagent.core :as reagent]
   [petrol.core :as petrol]
   [devtools.core :as devtools]
   ))

(defonce debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")
    (devtools/install!)
    ))

;; Model

(def initial-state {})

(def navlinks
  [[:a {:href "#/about"} "about"]
   [:a {:href "#/projects"} "projects"]
   [:a {:href "#/music"} "music"]
   [:a {:href "#/contact"} "contact"]])

(defonce app-state
  (reagent/atom initial-state))

;; View

(defn nav [links]
  [:div.nav
    [:ul.nav
     (for [l links]
       [:li.nav-item {:key l} l])]])

(defn joketicker [jokes]
  [:joke-ticker
   (get jokes (rand-int (count jokes)))])

(defn header [app children]
 [:div.container
  [:div.row.jumbotron.header [:a {:href "#/"} [:h1  "spar.bz"]]]
  [:div.row.joke-ticker [joketicker const/jokes]]
  [:div.row [nav navlinks]]
   children])

(defn content [children]
  [:div.row
   [:div.col-md-3 ""]
   [:div.col-md-6.content children]
   [:div.col-md-3 ""]])

(defn home [ui-channel app]
 [content "home"])

(defn about [ui-channel app]
 [content "about"])

(defn projects [ui-channel app]
 [content "projects"])

(defn music [ui-channel app]
 [content "music"])

(defn contact [ui-channel app]
 [content "contact"])


;; Routes

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (swap! app-state assoc :page :home))

  (defroute "/about" []
    (swap! app-state assoc :page :about))

  (defroute "/projects" []
    (swap! app-state assoc :page :projects))

  (defroute "/music" []
    (swap! app-state assoc :page :music))

  (defroute "/contact" []
    (swap! app-state assoc :page :contact))

  (hook-browser-navigation!))

;; Initialize App

(defmulti page identity)
(defmethod page :home [] home)
(defmethod page :about [] about)
(defmethod page :projects [] projects)
(defmethod page :music [] music)
(defmethod page :contact [] contact)
(defmethod page :default [] (fn [_] [:div]))

(defn current-page [ui-channel app]
  (let [page-key (:page app)]
    [header [app] [(page page-key) ui-channel app]]))

(defn reload []
  (swap! app-state identity))

(defn render-fn [ui-channel app]
  (reagent/render [current-page ui-channel app]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (app-routes)
  (petrol/start-message-loop! app-state render-fn))
