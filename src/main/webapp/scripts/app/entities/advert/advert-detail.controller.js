'use strict';

angular.module('dniprolabApp')
    .controller('AdvertDetailController', function ($scope, $rootScope, $stateParams, entity, Advert) {
        $scope.advert = entity;
        $scope.load = function (id) {
            Advert.get({id: id}, function(result) {
                $scope.advert = result;
            });
        };
        var unsubscribe = $rootScope.$on('dniprolabApp:advertUpdate', function(event, result) {
            $scope.advert = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
