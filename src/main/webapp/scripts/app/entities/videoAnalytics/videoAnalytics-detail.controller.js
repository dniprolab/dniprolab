'use strict';

angular.module('dniprolabApp')
    .controller('VideoAnalyticsDetailController', function ($scope, $rootScope, $stateParams, entity, VideoAnalytics, User) {
        $scope.videoAnalytics = entity;
        $scope.load = function (id) {
            VideoAnalytics.get({id: id}, function(result) {
                $scope.videoAnalytics = result;
            });
        };
        var unsubscribe = $rootScope.$on('dniprolabApp:videoAnalyticsUpdate', function(event, result) {
            $scope.videoAnalytics = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
