'use strict';

angular.module('dniprolabApp')
    .controller('ScheduleDetailController', function ($scope, $rootScope, $stateParams, entity, Schedule) {
        $scope.schedule = entity;
        $scope.load = function (id) {
            Schedule.get({id: id}, function(result) {
                $scope.schedule = result;
            });
        };
        var unsubscribe = $rootScope.$on('dniprolabApp:scheduleUpdate', function(event, result) {
            $scope.schedule = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
