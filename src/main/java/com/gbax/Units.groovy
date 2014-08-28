package com.gbax

import groovy.util.slurpersupport.GPathResult
import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

/**
 * Created by abayanov
 * Date: 27.08.14
 */
class Units {

    static def getHttp() {
        return new HTTPBuilder('http://localhost:8080')
    }

    def testIndex() {
        def http = getHttp()
        def html = http.get(path: '/index')
        println "done index"
        assert html instanceof GPathResult
        assert html.HEAD.size() == 1
        assert html.BODY.size() == 1
    }

    def testGetMessages() {
        def http = getHttp()
        final nextInt = new Random().nextInt(20)
        final s = '/topic/messages/' + nextInt
        http.request(GET) {
            uri.path = s
            uri.query = [page: 1, per_page: 5]
            response.success = { resp, json ->
                println "done | testGetMessages | ${s} | status" + resp.statusLine
                assert resp.statusLine.statusCode == 200
                assert json.readLine().size() > 0
            }
            response.failure = { resp ->
                if (resp.statusLine.statusCode != 404) {
                    println "testGetMessages: ${s} Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                } else {
                    println "done | testGetMessages | ${s} | status ${resp.statusLine.statusCode}"
                }

            }
        }
    }

    def testAddMessage() {
        def http = getHttp()
        final nextInt = new Random().nextInt(20)
        final s = "/topic/messages/" + nextInt

        http.request(POST) {
            uri.path = s
            send JSON, [message: "test " + nextInt]

            response.success = { resp ->
                println "done | testAddMessage | ${s} | status: ${resp.statusLine}"
                assert resp.statusLine.statusCode == 201
            }

            response.failure = { resp ->
                if (resp.statusLine.statusCode != 404) {
                    println "testAddMessage: ${s} Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                } else {
                    println "done | testAddMessage | ${s} | status: ${resp.statusLine}"
                }

                assert resp.statusLine.statusCode == 404
            }
        }
    }

    def testDelMessage() {
        def http = getHttp()
        final nextInt = new Random().nextInt(20)
        final s = "/topic/messages/" + nextInt + "/" + new Random().nextInt(400)
        try {
            http.request(DELETE) {
                uri.path = s

                response.success = { resp ->
                    println "done | testDelMessage | message ${s} | status: ${resp.statusLine}"
                    assert resp.statusLine.statusCode == 200
                }

                response.failure = { resp ->
                    if (resp.statusLine.statusCode != 404) {
                        println "testDelMessage: ${s} Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    } else {
                        println "done | testDelMessage | message ${s} | status: ${resp.statusLine}"
                    }
                    assert resp.statusLine.statusCode == 404
                }
            }
        } catch (e) {
            println e
        }
    }

    def testGetTopic() {
        def http = getHttp()
        final s = '/topic/' + new Random().nextInt(20)
        def html = http.get(path: s)
        println "done | testGetTopic | ${s}"
        assert html instanceof GPathResult
        assert html.HEAD.size() == 1
        assert html.BODY.size() == 1
    }

    def testGetTopics() {
        def http = getHttp()
        http.request(GET) {
            uri.path = '/topics'
            uri.query = [page: 1, per_page: 5]
            response.success = { resp, json ->
                println "done | testGetTopics | " + resp.statusLine
                assert resp.statusLine.statusCode == 200
                assert json.readLine().size() > 0
            }
            response.failure = { resp ->
                println "testGetTopics: Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
            }
        }
    }

    def testAddTopic() {
        def http = getHttp()
        final nextInt = new Random().nextInt(20)
        final s = "/topics"

        http.request(POST) {
            uri.path = s
            send JSON, [description: "test " + nextInt]

            response.success = { resp ->
                println "done | testAddTopic | ${nextInt} | status: ${resp.statusLine}"
                assert resp.statusLine.statusCode == 201
            }

            response.failure = { resp ->
                println "testAddTopic: ${s}. Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
            }
        }
    }

    def testDelTopic() {
        def http = getHttp()
        final nextInt = new Random().nextInt(20)
        final s = "/topics/" + nextInt
        try {
            http.request(DELETE) {
                uri.path = s

                response.success = { resp ->
                    println "done | testDelTopic | ${s} | status: ${resp.statusLine}"
                    assert resp.statusLine.statusCode == 200
                }

                response.failure = { resp ->
                    if (resp.statusLine.statusCode != 404) {
                        println "testDelTopic: ${s} Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
                    } else {
                        println "done | testDelTopic | ${s} | status: ${resp.statusLine}"
                    }
                    assert resp.statusLine.statusCode == 404
                }
            }
        } catch (e) {
            println e
        }
    }

    def doSomthing() {
        Random random = new Random();
        def whatDo = random.nextInt(8)
        switch (whatDo) {
            case 0:
                testIndex()
                break
            case 1:
                testGetMessages()
                break
            case 2:
                testAddMessage()
                break
            case 3:
                testDelMessage()
                break
            case 4:
                testGetTopic()
                break
            case 5:
                testGetTopics()
                break
            case 6:
                testAddTopic()
                break
            case 7:
                testDelTopic()
                break
            default:
                break
        }


    }

}
