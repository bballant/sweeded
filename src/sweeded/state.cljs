(ns sweeded.state
  (:require [reagent.core :as reagent]))

(def app-state (reagent/atom {}))
(def csv-data (reagent/atom nil))
