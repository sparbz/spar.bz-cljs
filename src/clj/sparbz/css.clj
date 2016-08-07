(ns sparbz.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body {:margin 0}]
  [:.header
   {:display "flex"
    :justify-content "center"}]
  [:.nav
   {:display "flex"
    :justify-content "center"}
   [:ul
    {:font-family "Courier"
     :list-style-type "none"
     :padding 0
     :margin 0}]]
  [:.jumbotron
   {:background "#FFF"
    :padding 0}]
  [:h1
   {:font-family "Courier New"
    :font-weight "500"}]
  [:p {:color "green"}])
