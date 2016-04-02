'use strict';

angular.module('dniprolabApp')
    .controller('VideoAnalyticsWidgetController', function($scope, $state, VideoAnalyticsWidget, ParseLinks){
        $scope.videoAnalyticss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            VideoAnalytics.query({page: $scope.page, size: 5, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
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
    }).directive('videoAnalyticWidgetDirective', function(){
        return {
            templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalyticWidget.html',
            controller: 'VideoAnalyticsWidgetController',
            restrict: 'E'
        };
    });
