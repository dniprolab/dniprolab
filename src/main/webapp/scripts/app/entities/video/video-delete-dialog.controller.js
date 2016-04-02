/**
 * Created by Overlord on 01.04.2016.
 */
'use strict';

angular.module('dniprolabApp')
    .controller('VideoDeleteController', function($scope, $uibModalInstance, entity, Video){
        $scope.video = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Message.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    });
