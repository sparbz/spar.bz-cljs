(ns sparbz.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:body
   {:margin 0
    :font-family "Courier"}
   [:a:link
    {:color "black"
     :text-decoration "none"}]]
  [:.header
   {:display "flex"
    :justify-content "center"
    :padding 0
    :margin 0}]
  [:.nav
   {:display "flex"
    :justify-content "center"}
   [:ul
    {:font-family "Courier"
     :list-style-type "none"
     :display "inline-flex"
     :padding 0
     :margin 0}]]
  [:.nav-item
   {:padding "0 5px 0 0"}]
  [:.joke-ticker
   {:display "flex"
    :justify-content "center"}]
  [:.jumbotron
   {:background "#FFF"
    :padding 0}]
  [:.content
   {:border "1px solid black"
    :text-align "center"}]
  [:h1
   {:font-family "Courier New"
    :font-weight "500"
    :margin "10px 0 0 0"}]
  [:p {:color "green"}])
