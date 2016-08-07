(ns sparbz.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [sparbz.core-test]))

(doo-tests 'sparbz.core-test)
