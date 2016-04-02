'use strict';

angular.module('dniprolabApp')
    .controller('VideoController', function($scope, $state, DateUtils, Video, VideoSearch, ParseLinks){
        $scope.videos = [];
        $scope.predicate = id;
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function () {
            Video.query({page: $scope.page, size: 20, sort:
                [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers){
                $scope.links = ParseLinks.parse(headers('link'));
                for(var i = 0; i < result.length; i++){
                    $scope.videos.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.messages = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            MessageSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.messages = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };
        $scope.clear = function () {
            $scope.video = {
                id: null,
                label: null,
                reference: null,
                description: null,
                author: null,
                created: null,
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });

