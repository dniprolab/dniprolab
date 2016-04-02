'use strict';

angular.app('dniprolabApp').controller('VideoDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Video', 'User',
    function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Video, User){

        $scope.video = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Video.get({id : id}, function(result) {
                $scope.video = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dniprolabApp:videoUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.message.id != null) {
                Video.update($scope.video, onSaveSuccess, onSaveError);
            } else {
                Video.save($scope.video, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.datePickerForCreated = {};

        $scope.datePickerForCreated.status = {
            opened: false
        };

        $scope.datePickerForCreatedOpen = function($event) {
            $scope.datePickerForCreated.status.opened = true;
        };

    }]);
