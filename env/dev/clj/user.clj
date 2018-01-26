(ns user
  (:require 
            [mount.core :as mount]
            [weather.figwheel :refer [start-fw stop-fw cljs]]
            [weather.core :refer [start-app]]))

(defn start []
  (mount/start-without #'weather.core/repl-server))

(defn stop []
  (mount/stop-except #'weather.core/repl-server))

(defn restart []
  (stop)
  (start))


