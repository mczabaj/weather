(ns weather.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [weather.core-test]))

(doo-tests 'weather.core-test)

