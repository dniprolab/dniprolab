'use strict';

angular.module('dniprolabApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('advertisement', {
                parent: 'entity',
                url: '/advertisements',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.advertisement.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/advertisement/advertisements.html',
                        controller: 'AdvertisementController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('advertisement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('advertisement.detail', {
                parent: 'entity',
                url: '/advertisement/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.advertisement.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/advertisement/advertisement-detail.html',
                        controller: 'AdvertisementDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('advertisement');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Advertisement', function($stateParams, Advertisement) {
                        return Advertisement.get({id : $stateParams.id});
                    }]
                }
            })
            .state('advertisement.new', {
                parent: 'advertisement',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/advertisement/advertisement-dialog.html',
                        controller: 'AdvertisementDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    text: null,
                                    author: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('advertisement', null, { reload: true });
                    }, function() {
                        $state.go('advertisement');
                    })
                }]
            })
            .state('advertisement.edit', {
                parent: 'advertisement',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/advertisement/advertisement-dialog.html',
                        controller: 'AdvertisementDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Advertisement', function(Advertisement) {
                                return Advertisement.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('advertisement', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('advertisement.delete', {
                parent: 'advertisement',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/advertisement/advertisement-delete-dialog.html',
                        controller: 'AdvertisementDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Advertisement', function(Advertisement) {
                                return Advertisement.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('advertisement', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
