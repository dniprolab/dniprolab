'use strict';

angular.module('dniprolabApp').controller('VideoAnalyticsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'VideoAnalytics', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, VideoAnalytics, User) {

        $scope.videoAnalytics = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            VideoAnalytics.get({id : id}, function(result) {
                $scope.videoAnalytics = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('dniprolabApp:videoAnalyticsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.videoAnalytics.id != null) {
                VideoAnalytics.update($scope.videoAnalytics, onSaveSuccess, onSaveError);
            } else {
                VideoAnalytics.save($scope.videoAnalytics, onSaveSuccess, onSaveError);
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
