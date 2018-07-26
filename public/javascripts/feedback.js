$(document).ready(function() {

  $('input[type="checkbox"], input[type="radio"]').click(function() {
    ga('send', 'event', this.id, 'click');
    if ( $('h1:first-child').attr('id') === "serviceReceived" ) {
      ga('send', 'event', {'dimension73': this.id}, 'click');
    }
  });

  $('[class*="checkboxgroup-clear"]').on('change', function() {
    if( $(this).is(':checked') ) {
      var classes = $(this).attr('class').split(' ');
      for ( i in classes ) {
        var c = classes[i];
        if ( c.startsWith('checkboxgroup-clear') )
          $('.'+c.replace('-clear', '')+':checked').prop('checked', false).trigger('change');
      }
    }
  });

});
