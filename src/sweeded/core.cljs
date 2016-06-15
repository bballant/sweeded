(ns sweeded.core
  (:require
    [sweeded.slides :refer [slides]]
    [sweeded.router :as r]))

(r/nav! (r/game-path))
