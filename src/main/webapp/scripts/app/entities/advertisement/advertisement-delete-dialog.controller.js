'use strict';

angular.module('dniprolabApp')
	.controller('AdvertisementDeleteController', function($scope, $uibModalInstance, entity, Advertisement) {

        $scope.advertisement = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Advertisement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
