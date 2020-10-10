(ns polylith.clj.core.change.environments-to-test
  (:require [clojure.set :as set]
            [polylith.clj.core.path-finder.interface.criterias :as c]
            [polylith.clj.core.path-finder.interface.select :as select]
            [polylith.clj.core.path-finder.interface.extract :as extract]))

(defn included-environments [{:keys [src-paths test-paths profile]} disk-paths]
  (let [path-entries (extract/path-entries [src-paths test-paths (:src-paths profile) (:test-paths profile)] disk-paths)]
    (select/names path-entries c/environment? c/test-path? c/exists?)))

(defn select-envs [env environments is-dev]
  (if is-dev
    environments
    (if (= "development" env)
      []
      (set/difference (set environments) #{"development"}))))

(defn env-tests [env changed-environments included-envs is-dev]
  (let [environments (set/intersection (set changed-environments)
                                       (set included-envs))]
    (select-envs env environments is-dev)))

(defn environments-to-test [{:keys [name is-run-tests] :as environment} disk-paths changed-environments is-dev is-run-env-tests is-all]
  (let [included-envs (included-environments environment disk-paths)]
    (cond
      is-all [name (vec (sort (select-envs name included-envs is-dev)))]
      (and is-run-tests is-run-env-tests) [name (vec (sort (env-tests name changed-environments included-envs is-dev)))]
      :else [name []])))

(defn env-to-environments-to-test [environments changed-environments disk-paths is-dev is-run-env-tests is-all]
  (into {} (map #(environments-to-test % disk-paths changed-environments is-dev is-run-env-tests is-all)
                environments)))
