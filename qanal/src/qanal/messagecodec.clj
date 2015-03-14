;; Licensed to the Apache Software Foundation (ASF) under one
;; or more contributor license agreements.  See the NOTICE file
;; distributed with this work for additional information
;; regarding copyright ownership.  The ASF licenses this file
;; to you under the Apache License, Version 2.0 (the
;; "License"); you may not use this file except in compliance
;; with the License.  You may obtain a copy of the License at
;;
;; http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.
(ns qanal.messagecodec
  (:require [cheshire.core :as json]
            [taoensso.timbre :as log]))


(defn decode-json [bytes]
  (with-open [rdr (clojure.java.io/reader bytes :encoding "UTF-8")]
    (try
      (json/parse-stream rdr keyword)
      (catch Exception e (log/warn "Unable to parse json due to this exception !! ->" e)))))

(defn validate-river-json [json-map]
  (let [{:keys [index type source]} json-map]
    (if (some nil? [index type source])
      (log/warn "The decoded json map doesn't represent a valid river-json. It should contain the following keys"
                " [index type source]. Decoded json map->" json-map)
      json-map)))

(defn decode-river-json [bytes]
  (if-let [json-map (decode-json bytes)]
    (validate-river-json json-map)))



(comment

  (def test-json (.getBytes "{\"index\":\"index-name\" , \"type\": \"my-type\", \"source\": \"my-source\"}"))
  (def json-map (decode-json test-json))
  (validate-river-json json-map)

  (decode-river-json test-json)
  )