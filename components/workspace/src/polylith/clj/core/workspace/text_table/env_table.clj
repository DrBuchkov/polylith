(ns polylith.clj.core.workspace.text-table.env-table
  (:require [polylith.clj.core.workspace.text-table.profile :as profile]
            [polylith.clj.core.text-table.interface :as text-table]
            [polylith.clj.core.util.interface.color :as color]
            [polylith.clj.core.path-finder.interface.extract :as extract]
            [polylith.clj.core.path-finder.interface.status :as status]))

(defn profile-cell [index env-name column show-resources? path-entries]
  (let [flags (status/env-status-flags path-entries env-name show-resources?)]
    (text-table/cell column (+ index 3) flags :purple :center)))

(defn profile-col [index profile ws-dir start-column settings environments show-resources?]
  (let [column (+ start-column (* 2 index))
        path-entries (extract/from-profiles-paths ws-dir settings profile)]
    (concat [(text-table/cell column 1 profile :purple :center :horizontal)]
            (map-indexed #(profile-cell %1 %2 column show-resources? path-entries)
                         (map :name environments)))))
(defn profile-columns [ws-dir start-column environments profiles settings show-resources?]
  (apply concat
         (map-indexed #(profile-col %1 %2 ws-dir start-column settings environments show-resources?)
                      profiles)))

(defn env-cell [environment env-key column row changed-environments color-mode]
  (let [name (env-key environment)
        changed (if (contains? (set changed-environments) name) " *" "")
        env (str (color/environment name color-mode)
                 changed)]
    (text-table/cell column row env)))

(defn env-column [environments {:keys [changed-environments]} header env-key column color-mode]
  (concat [(text-table/cell column header)]
          (map-indexed #(env-cell %2 env-key column (+ %1 3) changed-environments color-mode)
                       environments)))

(defn src-cell [index {:keys [name src-paths test-paths profile-src-paths profile-test-paths]} ws-dir environments-to-test show-resources?]
  (let [path-entries (extract/path-entries ws-dir [src-paths, test-paths profile-src-paths profile-test-paths])
        satus-flags (str (status/env-status-flags path-entries name show-resources?)
                         (if (contains? environments-to-test name) "x" "-"))]
    (text-table/cell 5 (+ index 3) satus-flags :purple :center)))

(defn src-column [ws-dir environments {:keys [env->environments-to-test]} show-resources?]
  (let [environments-to-test (set (mapcat second env->environments-to-test))]
    (concat [(text-table/cell 5 "source")]
            (map-indexed #(src-cell %1 %2 ws-dir environments-to-test show-resources?)
                         environments))))

(defn loc-cell [index lines-of-code column thousand-sep]
  (text-table/number-cell column (+ index 3) lines-of-code :right thousand-sep))

(defn loc-columns [show-loc? environments n#profiles thousand-sep total-col-src total-loc-test]
  (when show-loc?
    (let [column1 (+ 7 (* 2 n#profiles))
          column2 (+ 2 column1)]
      (concat [(text-table/cell column1 1 "loc" :none :right)]
              (map-indexed #(loc-cell %1 %2 column1 thousand-sep) (map :lines-of-code-src environments))
              [(text-table/number-cell column1 (+ (count environments) 3) total-col-src :right thousand-sep)]
              [(text-table/cell column2 1 "(t)" :none :right)]
              (map-indexed #(loc-cell %1 %2 column2 thousand-sep) (map :lines-of-code-test environments))
              [(text-table/number-cell column2 (+ (count environments) 3) total-loc-test :right thousand-sep)]))))

(defn table [{:keys [ws-dir settings environments changes]} show-loc? show-resources?]
  (let [{:keys [color-mode thousand-sep]} settings
        profiles (profile/all-profiles settings)
        n#profiles (count profiles)
        total-loc-src (apply + (filter identity (map :lines-of-code-src environments)))
        total-loc-test (apply + (filter identity (map :lines-of-code-test environments)))
        env-col (env-column environments changes "environment" :name 1 color-mode)
        alias-col (env-column environments {} "alias" :alias 3 color-mode)
        src-col (src-column ws-dir environments changes show-resources?)
        profile-cols (profile-columns ws-dir 7 environments profiles settings show-resources?)
        loc-col (loc-columns show-loc? environments n#profiles thousand-sep total-loc-src total-loc-test)
        space-columns (range 2 (* 2 (+ 3 (count profiles) (if show-loc? 2 0))) 2)
        header-spaces (text-table/spaces 1 space-columns (repeat "  "))
        cells (text-table/merge-cells env-col alias-col src-col loc-col profile-cols header-spaces)
        section-cols (if (or show-loc? (-> n#profiles zero? not)) [6 (+ 6 (* 2 n#profiles))] [])
        line-spaces (text-table/spaces 2 section-cols (repeat "   "))
        line (text-table/line 2 cells)]
    (text-table/table "  " color-mode cells line line-spaces)))

(defn print-table [workspace show-loc? show-resources?]
  (text-table/print-table (table workspace show-loc? show-resources?)))