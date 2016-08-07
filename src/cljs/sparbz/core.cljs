(ns sparbz.core
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
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

(def initial-state
  {:counter 0})

(def navlinks
  [[:a {:href "#/about"} "about page"]])

(defonce app-state
  (reagent/atom initial-state))


;; Update

(defrecord Decrement []
  petrol/Message
  (process-message [_ app]
    (update app :counter dec)))

(defrecord Increment []
  petrol/Message
  (process-message [_ app]
    (update app :counter inc)))


;; View

(defn nav [links]
  [:div.nav
    [:ul
     (for [l links]
       [:li {:key l} l])]])

(defn header [name]
 [:div.jumbotron [:a {:href "#/"} [:h1 name]]
  [nav navlinks]])



(defn home [ui-channel app]
  [:div.container
   [:div.header [header "sparbz"]]])


(defn about [ui-channel app]
  [:div.container
   [:div.header [header "sparbz"]]])


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

  ;; add routes here


  (hook-browser-navigation!))


;; Initialize App

(defmulti page identity)
(defmethod page :home [] home)
(defmethod page :about [] about)
(defmethod page :default [] (fn [_] [:div]))

(defn current-page [ui-channel app]
  (let [page-key (:page app)]
    [(page page-key) ui-channel app]))

(defn reload []
  (swap! app-state identity))

(defn render-fn [ui-channel app]
  (reagent/render [current-page ui-channel app]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (app-routes)
  (petrol/start-message-loop! app-state render-fn))
