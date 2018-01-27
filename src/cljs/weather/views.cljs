(ns weather.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ajax.core :refer [GET]]
            [weather.events]))

(defn table-header []
  [:tr
   [:th {:on-click #(rf/dispatch [:weather/sort-city])}
        "City"]
   [:th {:on-click #(rf/dispatch [:weather/sort-temp])}
        "Current Temperature"]])

(defn city-row []
   [:tr
    [:td "Boston"]
    [:td "GET TEMP HERE"]])

(defn city-table []
  [:table
    [table-header]
    [:tbody
      [city-row]]])

(defn debug-spot []
  [:p (str "see here:" @(rf/subscribe [:weather/cities]))])


(defn weather-page []
  (rf/dispatch [:weather/init-state])
  (fn []
    [:div.container
      [:h1 "Current Weather"]
      [city-table]
      [debug-spot]]))
