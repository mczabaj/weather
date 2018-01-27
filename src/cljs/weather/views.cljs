(ns weather.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ajax.core :refer [GET]]
            [weather.events]))

(defn table-header []
  [:thead
   [:tr
    [:th {:on-click #(rf/dispatch [:weather/sort-city])}
         "City"]
    [:th {:on-click #(rf/dispatch [:weather/sort-temp])}
         "Current Temperature"]]])

(defn city-rows [cities]
  [:tbody
   (for [c cities]
     [:tr {:key (:id c)}
       [:td (str (:name c))]
       [:td (str (:temp c))]])])

(defn city-table []
  [:table
    [table-header]
    [city-rows @(rf/subscribe [:weather/cities])]])

(defn weather-page []
  (rf/dispatch [:weather/init-state])
  (fn []
    [:div.container
      [:h1 "Current Weather"]
      [city-table]]))
