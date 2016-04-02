'use strict';

angular.module('dniprolabApp')
	.controller('AdvertDeleteController', function($scope, $uibModalInstance, entity, Advert) {

        $scope.advert = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Advert.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
