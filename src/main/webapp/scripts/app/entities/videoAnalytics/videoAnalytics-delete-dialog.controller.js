'use strict';

angular.module('dniprolabApp')
	.controller('VideoAnalyticsDeleteController', function($scope, $uibModalInstance, entity, VideoAnalytics) {

        $scope.videoAnalytics = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            VideoAnalytics.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
