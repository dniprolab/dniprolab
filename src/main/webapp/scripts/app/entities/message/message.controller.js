'use strict';

angular.module('dniprolabApp')
    .controller('MessageController', function ($scope, $state, DataUtils, Message, MessageSearch, ParseLinks) {

        $scope.messages = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Message.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    if(result[i].user == account.login) {
                        $scope.messages.push(result[i]);
                    }
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.messages = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            MessageSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.messages = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.message = {
                text: null,
                image: null,
                imageContentType: null,
                document: null,
                documentContentType: null,
                author: null,
                created: null,
                title: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
