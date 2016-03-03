'use strict';

angular.module('dniprolabApp')
	.controller('ScheduleDeleteController', function($scope, $uibModalInstance, entity, Schedule) {

        $scope.schedule = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Schedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
