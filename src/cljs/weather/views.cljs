(ns weather.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ajax.core :refer [GET]]
            [weather.events]))

(defn table-header []
  [:tr
    [:th {:on-click #(rf/dispatch [:weather/sort-city])}
      "City"]
    [:th "Current Temperature"]
    [:th "Weather Description"]])

(defn city-row []
  [:tr
    [:td "Boston"]
    [:td "GET TEMP HERE"]
    [:td "GET DESC HERE"]])

(defn city-table []
  [:table
    [table-header]
    [city-row]])

(defn weather-page []
  (rf/dispatch [:weather/init-state])
  (fn []
    [:div.container
      [:h1 "Current Weather"]

      [city-table]]))
