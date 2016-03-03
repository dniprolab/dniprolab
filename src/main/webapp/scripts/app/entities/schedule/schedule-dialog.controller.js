'use strict';

angular.module('dniprolabApp').controller('ScheduleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Schedule',
        function($scope, $stateParams, $uibModalInstance, entity, Schedule) {

        $scope.schedule = entity;
        $scope.load = function(id) {
            Schedule.get({id : id}, function(result) {
                $scope.schedule = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dniprolabApp:scheduleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.schedule.id != null) {
                Schedule.update($scope.schedule, onSaveSuccess, onSaveError);
            } else {
                Schedule.save($scope.schedule, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
