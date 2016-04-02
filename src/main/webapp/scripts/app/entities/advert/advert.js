'use strict';

angular.module('dniprolabApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('advert', {
                parent: 'entity',
                url: '/adverts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.advert.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/advert/adverts.html',
                        controller: 'AdvertController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('advert');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('advert.detail', {
                parent: 'entity',
                url: '/advert/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.advert.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/advert/advert-detail.html',
                        controller: 'AdvertDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('advert');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Advert', function($stateParams, Advert) {
                        return Advert.get({id : $stateParams.id});
                    }]
                }
            })
            .state('advert.new', {
                parent: 'advert',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/advert/advert-dialog.html',
                        controller: 'AdvertDialogController',
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
                        $state.go('advert', null, { reload: true });
                    }, function() {
                        $state.go('advert');
                    })
                }]
            })
            .state('advert.edit', {
                parent: 'advert',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/advert/advert-dialog.html',
                        controller: 'AdvertDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Advert', function(Advert) {
                                return Advert.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('advert', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('advert.delete', {
                parent: 'advert',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/advert/advert-delete-dialog.html',
                        controller: 'AdvertDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Advert', function(Advert) {
                                return Advert.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('advert', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
