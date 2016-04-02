'use strict';

angular.module('dniprolabApp')
    .controller('VideoAnalyticsController', function ($scope, $state, VideoAnalytics, VideoAnalyticsSearch, ParseLinks) {

        $scope.videoAnalyticss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            VideoAnalytics.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.videoAnalyticss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.videoAnalyticss = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            VideoAnalyticsSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.videoAnalyticss = result;
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
            $scope.videoAnalytics = {
                label: null,
                reference: null,
                description: null,
                author: null,
                date: null,
                id: null
            };
        };
    });
