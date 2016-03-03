'use strict';

angular.module('dniprolabApp')
    .controller('MessageDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Message, User) {
        $scope.message = entity;
        $scope.load = function (id) {
            Message.get({id: id}, function(result) {
                $scope.message = result;
            });
        };
        var unsubscribe = $rootScope.$on('dniprolabApp:messageUpdate', function(event, result) {
            $scope.message = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
