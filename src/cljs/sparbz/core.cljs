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
  [[:a {:href "#/about"} "about"]
   [:a {:href "#/projects"} "projects"]
   [:a {:href "#/music"} "music"]
   [:a {:href "#/contact"} "contact"]])

(def jokes
  ["now with less mediocrity."
   "not a community for longform non-fiction writers."
   "no, a 12-year-old did not make this site."
   "100% organic, certified GMO-free."
   "come for the chicken parm, stay for the terrible jokes."
   "putting the \"me\" in \"mediocre\" since 1991."
   "2003 bergen county middle school stock market champion."
   "30% off if you call right now."
   "the romantic comedy everyone is talking about."
   "gluten-free available upon request."
   "save 15% or more of your time by turning your TV off."
   "still not an albuquerque-based criminal attorney."
   "ask about our specials on bar-mitzvahs."
   "basically simba from the lion king."
   "this is not legal or medical advice."
   "purveyor of fine stuffed animals."])

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
    [:ul.nav
     (for [l links]
       [:li.nav-item {:key l} l])]])

(defn joketicker [jokes]
  [:joke-ticker
   (get jokes (rand-int (count jokes)))])
  ;  (js/setTimeout (fn [jokes] (get jokes (rand-int 2))) 1000)])

(defn header [app children]
 [:div.container
  [:div.row.jumbotron.header [:a {:href "#/"} [:h1  "spar.bz"]]]
  [:div.row.joke-ticker [joketicker jokes]]
  [:div.row [nav navlinks]]
   children])

(defn home [ui-channel app]
 [:div "home"])

(defn about [ui-channel app]
 [:div "about"])

(defn projects [ui-channel app]
 [:div "projects"])

(defn music [ui-channel app]
 [:div "music"])

(defn contact [ui-channel app]
 [:div "contact"])


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
