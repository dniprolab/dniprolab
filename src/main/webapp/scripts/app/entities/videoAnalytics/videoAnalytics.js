'use strict';

angular.module('dniprolabApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('videoAnalytics', {
                parent: 'entity',
                url: '/videoAnalyticss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.videoAnalytics.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalyticss.html',
                        controller: 'VideoAnalyticsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('videoAnalytics');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('videoAnalytics.detail', {
                parent: 'entity',
                url: '/videoAnalytics/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'dniprolabApp.videoAnalytics.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalytics-detail.html',
                        controller: 'VideoAnalyticsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('videoAnalytics');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'VideoAnalytics', function($stateParams, VideoAnalytics) {
                        return VideoAnalytics.get({id : $stateParams.id});
                    }]
                }
            })
            .state('videoAnalytics.new', {
                parent: 'videoAnalytics',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalytics-dialog.html',
                        controller: 'VideoAnalyticsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    label: null,
                                    reference: null,
                                    description: null,
                                    author: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('videoAnalytics', null, { reload: true });
                    }, function() {
                        $state.go('videoAnalytics');
                    })
                }]
            })
            .state('videoAnalytics.edit', {
                parent: 'videoAnalytics',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalytics-dialog.html',
                        controller: 'VideoAnalyticsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['VideoAnalytics', function(VideoAnalytics) {
                                return VideoAnalytics.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('videoAnalytics', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('videoAnalytics.delete', {
                parent: 'videoAnalytics',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/videoAnalytics/videoAnalytics-delete-dialog.html',
                        controller: 'VideoAnalyticsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['VideoAnalytics', function(VideoAnalytics) {
                                return VideoAnalytics.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('videoAnalytics', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
