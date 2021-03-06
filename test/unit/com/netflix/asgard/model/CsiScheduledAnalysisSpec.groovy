/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.asgard.model

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import spock.lang.Specification

class CsiScheduledAnalysisSpec extends Specification {

    Closure<String> jsonConstructor = { String state, String created ->
        """{
    "schedule": "true",
    "rest": "http://csi_explorers:8080/csi/rest/v1/?wtf=true&type=canaries",
    "cron": "0 0 0/1 * * ?",
    "state": "running",
    "scheduler": "cmccoy: us-east-1.prod: helloclay--test_instance_count-canary-analysis Tue Jan 21 17:29:33 GMT 2014",
    "parameters": {
        "trigger": "0 0 0/1 * * ?",
        "state" : "${state}",
        "schedulername": "cmccoy: us-east-1.prod: helloclay--test_instance_count-canary-analysis \
Tue Jan 21 17:29:33 GMT 2014",
        "created": "${created}"
    }
}"""
    }

    def 'should construct running ScheduledAnalysis from JSON'() {
        ScheduledAsgAnalysis expectedScheduledAnalysis = new ScheduledAsgAnalysis('cmccoy: us-east-1.prod: \
helloclay--test_instance_count-canary-analysis Tue Jan 21 17:29:33 GMT 2014', new DateTime(DateTimeZone.UTC).
                withDate(2014, 1, 21).withTime(17, 29, 33, 0))

        String json = jsonConstructor('running', 'Jan 21, 2014 5:29:33 PM')

        expect:
        new CsiScheduledAnalysisFactory().fromJson(json) == expectedScheduledAnalysis
    }

    def 'should construct paused ScheduledAnalysis from JSON'() {
        ScheduledAsgAnalysis expectedScheduledAnalysis = new ScheduledAsgAnalysis('cmccoy: us-east-1.prod: \
helloclay--test_instance_count-canary-analysis Tue Jan 21 17:29:33 GMT 2014', new DateTime(DateTimeZone.UTC).
                withDate(2013, 11, 1).withTime(3, 1, 55, 0))

        String json = jsonConstructor('paused', 'Nov 01, 2013 3:01:55 AM')

        expect:
        new CsiScheduledAnalysisFactory().fromJson(json) == expectedScheduledAnalysis
    }
}
