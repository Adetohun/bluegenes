(ns redgenes.sections.saveddata.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [redgenes.sections.saveddata.events]
            [reagent.core :as reagent]
            [redgenes.sections.saveddata.subs]
            [cljs-time.format :as tf]
            [accountant.core :refer [navigate!]]))


(def build-in-formatter (tf/formatter "HH:mm:ss dd/MM/YYYY"))

(defn sd []
  (fn [[id {:keys [created label type value] :as all}]]
    [:div.col
     [:div.saved-data-item.panel.panel-default
      [:div.panel-heading
       [:div.save-bar
        [:i.fa.fa-2x.fa-times]
        [:i.fa.fa-2x.fa-star]]]
      [:div.panel-body
       [:h3 (str label)]
       [:div (tf/unparse build-in-formatter created)]
       [:button.btn.btn-primary.btn-raised
        {:on-click (fn []
                     (dispatch ^:flush-dom [:results/set-query value])
                     (navigate! "#/results"))}
        "View"]]]]))

(defn main []
  (let [saved-data (subscribe [:saved-data/all])]
    (reagent/create-class
      {:component-did-mount
       (fn [e] (let [node (-> e reagent/dom-node js/$)]))
       :reagent-render
       (fn []
         [:div.container-fluid
          [:h1 "Saved Data"]
          [:div.container
           [:span "Today"]
           (into [:div.grid-4_md-3_sm-1.saved-data-container]
                 (map (fn [e] [sd e]) @saved-data))]])})))