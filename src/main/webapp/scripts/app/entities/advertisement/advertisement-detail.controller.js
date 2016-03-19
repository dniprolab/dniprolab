'use strict';

angular.module('dniprolabApp')
    .controller('AdvertisementDetailController', function ($scope, $rootScope, $stateParams, entity, Advertisement) {
        $scope.advertisement = entity;
        $scope.load = function (id) {
            Advertisement.get({id: id}, function(result) {
                $scope.advertisement = result;
            });
        };
        var unsubscribe = $rootScope.$on('dniprolabApp:advertisementUpdate', function(event, result) {
            $scope.advertisement = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
