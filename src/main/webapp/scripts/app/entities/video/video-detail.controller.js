/**
 * Created by Overlord on 01.04.2016.
 */
'use strict';

angular.module('dniprolabApp')
    .controller('VideoDetailController', function($scope, $rootScope, $stateParams, DataUtils, entity, Video, User){
        $scope.video = entity;
        $scope.load = function (id) {
            Message.get({id: id}, function(result) {
                $scope.message = result;
            });
        };
        var unsubscribe = $rootScope.$on('dniprolabApp:videoUpdate', function(event, result) {
            $scope.message = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });
