(ns rewrite-clj.zip
  (:refer-clojure :exclude [next find replace remove
                            seq? map? vector? list? set?
                            print map get assoc])
  (:require [rewrite-clj.zip
             [base :as base]
             [edit :as edit]
             [find :as find]
             [insert :as insert]
             [move :as move]
             [remove :as remove]
             [seq :as seq]
             [subedit :as subedit]
             [walk :as walk]
             [whitespace :as ws]]
            [rewrite-clj
             [parser :as p]
             [potemkin :refer [import-vars]]
             [node :as node]]
            [clojure.zip :as z]))

;; ## API Facade

(import-vars
  [clojure.zip
   node root]

  [rewrite-clj.zip.base
   child-sexprs
   edn* edn tag sexpr
   length value
   of-file of-string
   string root-string
   print print-root]

  [rewrite-clj.zip.edit
   replace edit splice
   prefix suffix]

  [rewrite-clj.zip.find
   find find-next
   find-depth-first
   find-next-depth-first
   find-tag find-next-tag
   find-value find-next-value
   find-token find-next-token]

  [rewrite-clj.zip.insert
   insert-right insert-left
   insert-child append-child]

  [rewrite-clj.zip.move
   left right up down prev next
   leftmost rightmost
   leftmost? rightmost? end?]

  [rewrite-clj.zip.remove
   remove]

  [rewrite-clj.zip.seq
   seq? list? vector? set? map?
   map map-keys map-vals
   get assoc]

  [rewrite-clj.zip.subedit
   edit-node edit-> edit->>
   subedit-node subedit-> subedit->>]

  [rewrite-clj.zip.walk
   prewalk
   postwalk]

  [rewrite-clj.zip.whitespace
   whitespace? linebreak?
   whitespace-or-comment?
   skip skip-whitespace
   skip-whitespace-left
   prepend-space append-space
   prepend-newline append-newline])

;; ## Base Operations

(defmacro ^:private defbase
  [sym base]
  (let [{:keys [arglists]} (meta
                             (ns-resolve
                               (symbol (namespace base))
                               (symbol (name base))))
        sym (with-meta
              sym
              {:doc (format "Directly call '%s' on the given arguments." base)
               :arglists `(quote ~arglists)})]
    `(def ~sym ~base)))

(defbase right* clojure.zip/right)
(defbase left* clojure.zip/left)
(defbase up* clojure.zip/up)
(defbase down* clojure.zip/down)
(defbase next* clojure.zip/next)
(defbase prev* clojure.zip/prev)
(defbase rightmost* clojure.zip/rightmost)
(defbase leftmost* clojure.zip/leftmost)
(defbase replace* clojure.zip/replace)
(defbase edit* clojure.zip/edit)
(defbase remove* clojure.zip/remove)

;; ## DEPRECATED

(defn ^{:deprecated "0.4.0"} ->string
  "DEPRECATED. Use `string` instead."
  [zloc]
  (string zloc))

(defn ^{:deprecated "0.4.0"} ->root-string
  "DEPRECATED. Use `root-string` instead."
  [zloc]
  (root-string zloc))
