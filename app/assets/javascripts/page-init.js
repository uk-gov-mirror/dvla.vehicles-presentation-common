define(function(require) {
    var $ = require('jquery'),
        addressLookup = require('address-picker');

    var disableSubmitOnClick = function() {
        $(':submit').on('click', function() {
            if ( $(this).hasClass("disabled") ) {
                return false;
            }
            $(this).html('Loading').addClass('loading-action disabled');
            var runTimes = 0;
            setInterval(function() {
                if ( runTimes < 3 ){
                    $(':submit').append('.');
                    runTimes++;
                } else {
                    runTimes = 0;
                    $(':submit').html('Loading');
                }
            }, 1000);
        });
    };

    var closingWarning = function() {
        var d = new Date(),
            h = d.getHours(),
            m = d.getMinutes(),
            s = d.getSeconds(),
            closingWaring = $('.serviceClosingWarning'),
            closingHour = 17,
            closingMinute = 59,
            closingMinuteFinalWarning = 55,
            closingMinuteStart = 45,
            minLeft,
            secLeft;

        if ((h == closingHour) && (m >= closingMinuteStart) && (h == closingHour) && (m <= closingMinute)) {

            var refreshTimer = setInterval(function () {
                var dClosing = new Date();

                var mClosing = dClosing.getMinutes();

                var sClosing = dClosing.getSeconds();
                minLeft = closingMinute - mClosing;
                secLeft = 60 - sClosing;

                $('.js-minutes-left').html(minLeft);
                $('.js-seconds-left').html(secLeft - 1);

                if ((h == closingHour) && (mClosing >= closingMinuteFinalWarning) && (mClosing <= closingMinute)) {
                    closingWaring.removeClass('closing-warning');
                    closingWaring.addClass('final-closing-warning');
                    if ((h == closingHour) && (mClosing == closingMinute) && (sClosing >= 57)) {
                        closingWaring.removeClass('closing-warning');
                        closingWaring.addClass('final-closing-warning');
                        $('.serviceClosingWarning p').html('Service now closed. Service is available 08:00 to 18:00 Monday to Saturday.');
                        clearInterval(refreshTimer);
                    }
                }

            }, 1000);

            $('.serviceClosingWarning p').html('This service is available from 08:00 to 18:00, you have <span class="js-minutes-left">15</span>:<span class="js-seconds-left">00</span> to complete this service.');

        } else {
            closingWaring.removeClass('closing-warning');
            closingWaring.addClass('final-closing-warning');
            $('.serviceClosingWarning p').html('Service now closed. Service is available 08:00 to 18:00 Monday to Saturday.');

        }
    }
    var openFeedback = function(inputId, event) {
        var element = document.getElementById(inputId);
        if (element) {
            if (element.addEventListener) {
                // addEventListener is a W3 standard that is implemented in the majority of other browsers (FF, Webkit, Opera, IE9+)
                element.addEventListener(event, function (e) {
                    console.log("openFeedback addEventListener id: " + inputId + ", event " + event);
                    //window.open(url,'_blank');
                    window.open(this.href, '_blank');
                    e.preventDefault();
                });
            } else if (element.attachEvent) {
                // attachEvent can only be used on older trident rendering engines ( IE5+ IE5-8*)
                element.attachEvent(event, function (e) {
                    // console.log("openFeedback addEventListener id: " + inputId + ", event " + event);
                    //window.open(url,'_blank');
                    window.open(this.href, '_blank');
                    e.preventDefault();
                });
            } else {
                console.error("element does not support addEventListener or attachEvent");
                return false;
            }
        } else {
            console.error("element id: " + inputId + " not found on page");
            return false;
        }
    };

    var autoTabForInputs = function() {
        // Auto-tab for date format forms and document ref number input

        var hasFocus = $('.form-date input, #documentReferenceNumber').prop("autofocus");
        if (hasFocus) {
            $('.form-date input, #documentReferenceNumber').focus();
        }
        $('.form-date input, #documentReferenceNumber').one('focus', function() {
            var nextInput, focusMaxLength, currentLength;
            // Getting next field
            nextInput = $(':input:eq(' + ($(':input').index(this) + 1) + ')');
            // Getting focus max length
            focusMaxLength = $(this).attr('maxlength');
            // On keyup function
            $(this).on('keyup', function(e) {
                // Getting keycode from event
                keyCode = e.keyCode || e.which;
                // If back-tab (shift+tab)
                if ((keyCode == 9) && (e.shiftKey)){
                    // browse backwards through the form and empty input content
                    $(':input:eq(' + ($(':input').index(this)) + ')').focus().val('');
                    return false;
                }
                // check if limit has been reached and move to next input
                else
                {
                    currentLength = $(this).val().length;
                    if (focusMaxLength == currentLength){
                        nextInput.focus();
                        return false;
                    }
                }
            });
        });
    };

    var disableClickOnDisabledButtons = function() {
        $('.button-not-implemented').click(function() {
            return false;
        });
    };

    var printButton = function() {
        $('.print-button').click(function() {
            window.print();
            return false;
        });
    };

    var enableSmoothScroll = function() {
        $('a[href^="#"]').bind('click.smoothscroll', function (e) {
            e.preventDefault();
            var target = this.hash,
                $target = $(target);
            $('html, body').animate({
                scrollTop: $(target).offset().top - 40
            }, 750, 'swing', function () {
                window.location.hash = target;
            });
        });
    };

    var feedbackFormCharacterCountdown = function() {
        if ($("#feedback-form textarea").length > 0) {
            function updateCountdown() {
                // 500 is the max message length
                var remaining = 500 - $('#feedback-form textarea').val().length;
                $('.character-countdown').text(remaining);
            }
            $(document).ready(function($) {
                // IE 9- maxlength on input textarea
                var txts = document.getElementsByTagName('TEXTAREA')
                for(var i = 0, l = txts.length; i < l; i++) {
                    if(/^[0-9]+$/.test(txts[i].getAttribute("maxlength"))) {
                        var func = function() {
                            var len = parseInt(this.getAttribute("maxlength"), 10);

                            if(this.value.length > len) {
                                this.value = this.value.substr(0, len);
                                return false;
                            }
                        };
                        txts[i].onkeyup = func;
                        txts[i].onblur = func;
                    }
                }
                // Update Countdown on input textarea
                $('#feedback-form textarea').change(updateCountdown);
                $('#feedback-form textarea').keyup(updateCountdown);
            });
        }
    };

    var enableOptionToggle = function() {
        $('.optional-field').hide();

        $('.expandable-optional .option-visible').on('click', function() {
            $(this).closest('.expandable-optional').find('.optional-field').show(100);
        });
        $('.expandable-optional .option-invisible').on('click', function() {
            $(this).closest('.expandable-optional').find('.optional-field').hide(100);
        });

        $('.expandable-optional .option-visible:checked').click();
    };

    // TODO: remove it if unused
    var areCookiesEnabled = function() {
        var cookieEnabled = (navigator.cookieEnabled) ? true : false;

        if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
        {
            document.cookie="testcookie";
            cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
        }
        return (cookieEnabled);
    };

    var formCheckedSelection = function() {
        var label = $('label.form-radio.selectable, label.form-checkbox.selectable');

        label.each(function() {
            var input = $(this).children('input:radio, input:checkbox');
            input.on('change', function() {
                if(input.is(':checked')) {
                    $('label').removeClass('selected');
                    $(this).parent('label').addClass('selected');
                    input.parent(label).addClass('selected');
                }
                if(!input.is(':checked')) {
                    input.parent(label).removeClass('selected');
                }
            });
        });
    };

    var hideEmailOnOther = function(radioOtherId, emailId) {
        if (!radioOtherId.length || !emailId.length) {
            return;
        }

        var checkStateOfRadio = function(radioOtherId, emailId) {
            if(!$(radioOtherId).attr('checked')) {
                $(emailId).parent().hide().removeClass('item-visible');
                $(emailId).val('');
            } else {
                $(emailId).parent().show().addClass('item-visible');
            }
        };

        checkStateOfRadio(radioOtherId, emailId);

        $("input:radio" ).click(function() {
            checkStateOfRadio(radioOtherId, emailId);
        });
    };

    var imageHintToggles = function() {
        $('.hint-image-wrap > .panel-indent-wrapper').hide();

        $('.hint-image-wrap > p').on('click', function() {
            $(this).siblings().toggle();
        });
    };

    return {
        disableSubmitOnClick: disableSubmitOnClick,
        closingWarning: closingWarning,
        openFeedback: openFeedback,
        autoTabForInputs: autoTabForInputs,
        imageHintToggles: imageHintToggles,
        disableClickOnDisabledButtons: disableClickOnDisabledButtons,
        printButton: printButton,
        enableSmoothScroll: enableSmoothScroll,
        feedbackFormCharacterCountdown: feedbackFormCharacterCountdown,
        enableOptionToggle: enableOptionToggle,
        formCheckedSelection: formCheckedSelection,
        hideEmailOnOther: hideEmailOnOther, // Do not call this from initAll because only some exemplars need it
        initAll: function() {
            $(function() {
                disableSubmitOnClick();
                closingWarning();
                autoTabForInputs();
                imageHintToggles();
                disableClickOnDisabledButtons();
                printButton();
                enableSmoothScroll();
                feedbackFormCharacterCountdown();
                enableOptionToggle();
                formCheckedSelection();

                if ($('#feedback-open').length) {
                    openFeedback('feedback-open', 'click');
                }

                //html5 autofocus fallback for browsers that do not support it natively
                //if form element autofocus is not active, autofocus
                $('[autofocus]:not(:focus)').eq(0).focus();

                // Disabled clicking on disabled buttons
                $('.button-not-implemented').click(function() {
                    return false;
                });

                $("#tryagain").click(function() {
                    if($(this).hasClass("disabled")) return false;
                    $(this).addClass("disabled");
                    return true;
                });
                addressLookup.enableAddressLookup()
            });
        }
    };
});
