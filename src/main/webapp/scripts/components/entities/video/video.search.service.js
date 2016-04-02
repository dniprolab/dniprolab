/**
 * Created by Overlord on 01.04.2016.
 */

'use strict';

angular.module('dniprolabApp')
    .factory('VideoSearch', function($resource){
        return $resource('api/_search/video/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
