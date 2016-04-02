'use strict';

angular.module('dniprolabApp').controller('AdvertDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Advert',
        function($scope, $stateParams, $uibModalInstance, entity, Advert) {

        $scope.advert = entity;
        $scope.load = function(id) {
            Advert.get({id : id}, function(result) {
                $scope.advert = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dniprolabApp:advertUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.advert.id != null) {
                Advert.update($scope.advert, onSaveSuccess, onSaveError);
            } else {
                Advert.save($scope.advert, onSaveSuccess, onSaveError);
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
