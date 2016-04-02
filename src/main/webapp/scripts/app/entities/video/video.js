'use strict';

angular.module('dniprolabApp')
    .config(function($stateProvider){
        $stateProvider
            .state('video', {
                parent: 'entity',
                url: '/videos',
                data: {
                    authorities : ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.video.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/message/videos.html',
                        controller: 'VideoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('video');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('video.detail', {
                parent: 'entity',
                url: '/video/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.video.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/video/video-detail.html',
                        controller: 'VideoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('video');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Message', function($stateParams, Message) {
                        return Message.get({id : $stateParams.id});
                    }]
                }
            })
            .state('video.new', {
                parent: 'video',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/video/video-dialog.html',
                        controller: 'VideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null,
                                    label: null,
                                    reference: null,
                                    description: null,
                                    author: null,
                                    created: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                            $state.go('video', null, { reload: true });
                        }, function() {
                            $state.go('video');
                        })
                }]
            })
            .state('video.edit', {
                parent: 'video',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/video/video-dialog.html',
                        controller: 'VideoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Video', function(Message) {
                                return Message.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('video', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        })
                }]
            })
            .state('video.delete', {
                parent: 'video',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/video/video-delete-dialog.html',
                        controller: 'VideoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Video', function(Message) {
                                return Message.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('video', null, { reload: true });
                        }, function() {
                            $state.go('^');
                        })
                }]
            });
    });
