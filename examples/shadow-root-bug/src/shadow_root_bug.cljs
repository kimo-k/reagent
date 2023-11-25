(ns shadow-root-bug
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdomc]))

(defonce shadow-root
  (let [id "shadow-root"
        container (.getElementById js/document id)]
    (let [body        (.-body js/document)
          container   (.createElement js/document "div")
          shadow-root (.attachShadow container #js {:mode "open"})]
      (.setAttribute container "id" id)
      (.appendChild body container)
      (rdomc/create-root shadow-root))))

(defn repro []
  (let [text (r/atom "")]
    (fn []
      [:input {:on-change #(reset! text (.-value (.-target %)))
               :value @text}])))

(defonce normal-root (rdomc/create-root (js/document.getElementById "main-content")))

(defn ^:dev/after-load init! []
  (rdomc/render normal-root [:<> "This works:" [repro]])
  (rdomc/render shadow-root [:<> "This loses the cursor:" [repro]]))

(init!)
