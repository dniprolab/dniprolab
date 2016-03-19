'use strict';

angular.module('dniprolabApp').controller('AdvertisementDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Advertisement',
        function($scope, $stateParams, $uibModalInstance, entity, Advertisement) {

        $scope.advertisement = entity;
        $scope.load = function(id) {
            Advertisement.get({id : id}, function(result) {
                $scope.advertisement = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dniprolabApp:advertisementUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.advertisement.id != null) {
                Advertisement.update($scope.advertisement, onSaveSuccess, onSaveError);
            } else {
                Advertisement.save($scope.advertisement, onSaveSuccess, onSaveError);
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
