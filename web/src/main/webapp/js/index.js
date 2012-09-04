var app = null;

$(window).load(function() {

   app = new Application();
   app.init();

});     


/* */
var Application = function() {

   var showcase = new Showcase();

   this.init = function() {   
      showcase.init();      
   }
   
   this.load = function() {

      var url = 'index/' + showcase.getTab().getCode() + '/' + showcase.getPage();
      $.ajax({
         url: url,
         type: 'GET',
         dataType: 'json',
         error : function() {
            showcase.error();
         }
      })
      .done(function(msg) {
         showcase.makePage(msg);
      });   

   }

   this.manage = function(manageType, data) {
   
      var url = 'index/' + showcase.getTab().getCode() + 
                     '/' + showcase.getPage() + 
                     '/' + manageType;

      $.ajax({
         url: url,
         type: 'GET',
         dataType: 'json',
         data: data,
         error : function() {
            showcase.error();
         }
      })
      .done(function(msg) {
         showcase.makePage(msg);
      });
   }

};

Application.MANAGE_TYPE = 
{ 
   ADD    : 'add', 
   EDIT   : 'edit', 
   DELETE : 'delete',
   SEARCH : 'search', 
};

Application.ERROR_TYPE = 
{
   ERR_NONE   : { NAME : 'ERR_NONE',   MESSAGE : '' },   
	ERR_EDT_AC : { NAME : 'ERR_EDT_AC', MESSAGE : 'Element already exists' }, 
	ERR_EDT_CF : { NAME : 'ERR_EDT_CF', MESSAGE : 'No such element' },
	ERR_EDT_ID : { NAME : 'ERR_EDT_ID', MESSAGE : 'Error: Element has invalid dependencies' }, 
	ERR_ADD_AC : { NAME : 'ERR_ADD_AC', MESSAGE : 'Element already exists' }, 
	ERR_ADD_ID : { NAME : 'ERR_ADD_ID', MESSAGE : 'Error: Element has invalid dependencies' }, 
	ERR_DEL_CF : { NAME : 'ERR_DEL_CF', MESSAGE : 'No such element' }, 
	ERR_SCH_IE : { NAME : 'ERR_SCH_IE', MESSAGE : 'Error: Internal error' }, 
	ERR_SCH_ID : { NAME : 'ERR_SCH_ID', MESSAGE : 'Error: Element has invalid dependencies' }, 
	ERR_SCH_CF : { NAME : 'ERR_SCH_CF', MESSAGE : "Can't find element with such parameters" },
};


/* */
var Showcase = function() {  

   /* Showcase Informer */
   var ShowcaseInformer = function() {

      var DOMObject = $(document.createElement('div'))
      .appendTo($('#showcase-informer'));

      this.info = function(message, callback) {
         $(DOMObject)
         .empty()
         .append(message)
         .slideDown('fast',callback);
      }

      this.warning = function(message, callback) {
         $(DOMObject)
         .empty()
         .append(message)
         .slideDown('fast')
         .delay(2000)
         .slideUp('fast',callback);
      }

      this.clear = function(callback) {
         $(DOMObject)
         .slideUp('fast',callback);
      }
   };

   ShowcaseInformer.MESSAGES = 
   {
      ERROR : 'Got error while load data',
      WAIT  : 'Wait a minute...',
   };

   /* Showcase Tab */
   var ShowcaseTab = function(code) {
      
      this.getCode = function() {
         return code;
      }

      this.select = function() {
         $(DOMObject).addClass('selected');
      }
      
      this.deselect = function() {
         $(DOMObject).removeClass('selected');
      }

      this.selected = function() {
         return $(DOMObject).hasClass('selected');
      }

      this.block = function() {
         $(DOMObject).addClass('blocked');
      }

      this.unblock = function() {
         $(DOMObject).removeClass('blocked');
      }

      this.blocked = function() {
         return $(DOMObject).hasClass('blocked');
      }

      this.click = function() {

         if(!self.selected() && !self.blocked()) {
            informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
               showcase.deactivateButtons();
               showcase.clearMenu();
               showcase.blockMenu();
               showcase.clear();
               self.select();      
               tab = self;
               page = 0;
               app.load();
            });
         }

      }

      var self = this;
      var DOMObject = $('#showcase-tab-' + code);
      
      if(DOMObject != undefined) { 
         DOMObject
         .click(self.click)
         .mouseover(function() {
            if(!self.selected() && !self.blocked()) {
               $(this).addClass('hover'); 
            }
         })
         .mouseout(function() {
            $(this).removeClass('hover');
         });
      }

   };

   ShowcaseTab.CODES = 
   { 
      OBJ : 'obj',
      CLS : 'cls',
      STL : 'stl',
      FAM : 'fam',
      CSA : 'csa', 
      OBV : 'obv',
   };


   /* Showcase Button */
   var ShowcaseButton = function(code) {
      
      var DOMObject = $('#showcase-button-' + code);
      addMouseHoverEvents(DOMObject, true);

      this.click = function(listener) {
         $(DOMObject).click(listener);
      }

      this.block = function() {
         $(DOMObject).addClass('blocked');
      }

      this.unblock = function() {
         $(DOMObject).removeClass('blocked');
      }

      this.isBlocked = function() {
         return $(DOMObject).hasClass('blocked');
      }

   };

   ShowcaseButton.CODES =
   {
      SCH : 'sch',
      ADD : 'add',
      UP  : 'up',
      DWN : 'dwn',
      FST : 'fst',
      LST : 'lst',
   }

   /* Showcase Search */
   var ShowcaseSearch = function() {

      var menu = $('#showcase-form-sch');
      var type = $('#showcase-form-sch-type');
      var input = $('#showcase-form-sch-input');
      var button = $('#showcase-form-sch-button');
      var isVisible = false;
      
      $(menu).hide();
      

      this.show = function() {
         $(menu).slideDown('fast');
          isVisible = true;
      }

      this.hide = function() {
         $(menu).slideUp('fast');
          isVisible = false;
      }

      this.isVisible = function() {
         return isVisible;
      }

      this.create = function(tab) {
         switch(tab) {
            case ShowcaseTab.CODES.OBJ:
               $(type).empty()
               .append('<option>by object name</option>')
               .append('<option>by class name</option>');
               break;
            case ShowcaseTab.CODES.OBV:
                $(type).empty()
               .append('<option>by object name</option>')
               .append('<option>by style name</option>');
               break;
            default:
               return;
         }
         addMouseHoverEvents($(button), false);
         $(button).click(function() {
            var typeVal = $(type).val()
            .split(' ')
            .join('')
            .toLowerCase();;
            var inputVal = $(input).val();
            if(typeVal.length == 0 || inputVal.length == 0 || inputVal == 'Unknown')
               return;
            var json = { type : typeVal, value : inputVal };
            showcase.deactivateButtons();
            showcase.blockMenu();
            showcase.clear();
            informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
               app.manage(Application.MANAGE_TYPE.SEARCH, json);
            });
         });
      }

   };

   /* Showcase Element */
   var ShowcaseElement = function() {
      
      var Header = function() { 
         
         var container = $(document.createElement('div'))
         .attr('id','showcase-element-header');

         this.create = function(headerValue) {
            $(container).attr('class','button')
            .click(function() {
               if(!body.isActivated()) {
                  if(!body.isExpanded()) {
                     $(this).addClass('selected'); 
                     body.show();
                  }
                  else {
                     var header = $(this);
                     body.hide(true, function() {
                        header.removeClass('selected'); 
                     });
                  }
               }
            })
            .mouseover(function() {
               if(!body.isActivated()) {
                  $(this).addClass('hover'); 
               }
            })
            .mouseout(function() {
               $(this).removeClass('hover');
            })
            .append(headerValue);
         }

         this.appendTo = function(elementContainer) {
            $(elementContainer).append(container);
         }

         this.clearSelection = function() {
            if(!body.isExpanded()) {
               $(container).removeClass('selected');
            }
         }
         

      };

      var Body = function() {
         
         var BodyField = function(header, value, editable, type) {

            var isEditable = editable;
            var headerDOMObject = $(document.createElement('div'))
            .attr('class','showcase-element-body-field-name')
            .append(header);
            var valueDOMObject = $(document.createElement('div'))
            .attr('class','showcase-element-body-field-value')
            .append(value);

            $(container)
            .append(headerDOMObject)
            .append(valueDOMObject);

            this.makeEditable = function() {
               if(isEditable) {
                  var input = $(document.createElement(type))
                  .attr('maxlength',200)
                  .val(value);
                  $(valueDOMObject)
                  .empty()
                  .append(input);
               }
            }

            this.makeStatic = function() {
               if(isEditable) {
                  $(valueDOMObject)
                  .empty()
                  .append(value);
               }
            }

            this.getValue = function() {
               if(isEditable) {
                  return $(valueDOMObject)
                  .children()
                  .val();
               }
                return $(valueDOMObject)
               .text();
            }

            this.getHeader = function() {
               return header;
            }
           
         }

         var BodyButton = function(name, clickCallback, maskName, maskCallback) {

            var isMasked = false;
            var name = name;
            var maskName = maskName;
            var clickCallback = clickCallback;
            var maskCallback = maskCallback;
            var container = $(document.createElement('div'))
            .attr('id','showcase-element-body-manage-panel-button')
            .click(clickCallback)
            .append(name);
            addMouseHoverEvents(container, false);

            this.change = function() {
               if(!isMasked) {
                  $(container).empty()
                  .unbind('click')
                  .click(maskCallback)
                  .append(maskName)
                  .addClass('mask');
                  isMasked = true;
               }
               else {
                  $(container).empty()
                  .unbind('click')
                  .click(clickCallback)
                  .removeClass('mask')
                  .append(name);
                  isMasked = false;
               }
            }

            this.appendTo = function(buttonsContainer) {
               $(buttonsContainer).append(container);
            }

         }

         var BodyManagePanel = function(body) {

            var self = this;
            var container = $(document.createElement('div'))
            .attr('id','showcase-element-body-manage-panel');
            var infoLabel = $(document.createElement('div'))
            .attr('id','showcase-element-body-manage-panel-info-label');
            var selectedAction = '';

            var buttons = { 
               'edit' : new BodyButton('Edit', function() {
                  self.change('Edit:','edit');
                  if(selectedAction == 'edit') {
                     body.makeEditable();
                  }
               }, 'Ok', function() {
                  if(selectedAction == 'edit') {
                     var json = { id: data.id };
                     for(var field in fields) {
                        if(fields[field].getValue().length == 0) {
                           self.warning('Edit:','Incorrect data!');
                           return;
                        }
                        else if(fields[field].getValue() == 'Unknown') {
                           self.warning('Edit:','Cannot edit \'Unknown\' values!');
                           return;
                        }
                        else {
                           var jsonParamName = fields[field]
                           .getHeader()
                           .split(' ')
                           .join('')
                           .toLowerCase();
                           json[jsonParamName] = fields[field].getValue();
                        }
                     }
                     body.hide(true, function() { 
                        body.makeStatic();
                        header.clearSelection();
                        showcase.deactivateButtons();
                        showcase.blockMenu();
                        showcase.clear();
                        informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
                           app.manage(Application.MANAGE_TYPE.EDIT,json);
                        });
                     }); 
                  }
                  else if(selectedAction == 'delete') {
                     var json = { id: data.id };
                     body.hide(true, function() {
                        header.clearSelection();
                        showcase.deactivateButtons();
                        showcase.blockMenu();
                        showcase.clear();
                        informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
                           app.manage(Application.MANAGE_TYPE.DELETE, json);
                        });
                     });
                  }
                  else if(selectedAction == 'add') {
                     var json = { id: -1 };
                     for(var field in fields) {
                        if(fields[field].getValue().length == 0) {
                           self.warning('Add:','Incorrect data!');
                           return;
                        }
                        else if(fields[field].getValue() == 'Unknown') {
                           self.warning('Add:','Cannot edit \'Unknown\' values!');
                           return;
                        }
                        else {
                           var jsonParamName = fields[field]
                           .getHeader()
                           .split(' ')
                           .join('')
                           .toLowerCase();
                           json[jsonParamName] = fields[field].getValue();
                        }
                     }
                     body.hide(true, function() {
                        showcase.removeAdded(); 
                        header.clearSelection();
                        showcase.deactivateButtons();
                        showcase.blockMenu();
                        showcase.clear();
                        informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
                           app.manage(Application.MANAGE_TYPE.ADD,json);
                        });
                     }); 
                  }
                  self.change('','');
               }),
               'delete' : new BodyButton('Delete', function() {
                  self.change('Delete:','delete');
               }, 'Cancel', function() {
                  if(selectedAction == 'edit') {
                     body.makeStatic();
                  }
                  else if(selectedAction == 'add') {
                     body.hide(true, function() {
                        header.clearSelection();
                        showcase.removeAdded();
                     });
                     return;
                  }
                  self.change('','');
               }),
            }

            this.change = function(message, actionName) {
               selectedAction = actionName;
               buttons['edit'].change();
               buttons['delete'].change();
               this.info(message);
            }

            this.info = function(message) {
               $(infoLabel).fadeOut(100, function() {
                  $(this).empty()
                  .append(message)
                  .fadeIn(100);
               });
            }

            this.warning = function(message, warn) {
               $(infoLabel).fadeOut(100, function() {
                  $(this).empty()
                  .append(warn)
                  .fadeIn(100)
                  .delay(1000)
                  .fadeOut(100, function() {
                     $(this).empty()
                     .append(message)
                     .fadeIn(100);
                  }); 
               });
            }

            this.create = function(bodyContainer, isForAdding) {
               buttons['edit'].appendTo(container);
               buttons['delete'].appendTo(container);
               if(isForAdding)
                  this.change('Add','add');
               $(container).append(infoLabel);
               $(bodyContainer).append(container);
            }        

         }

         var fields = new Array();
         var isExpanded = true;
         var isActivated = false;
         var self = this;
         var managePanel = new BodyManagePanel(this);
         var container = $(document.createElement('div'))
         .attr('id','showcase-element-body');

         this.appendTo = function(elementContainer) {
            $(elementContainer).append(container);
         }
      
         this.show = function() {
            $(container).slideDown(300);
            isExpanded = true;
         }

         this.hide = function(animate, callback) {
            if(animate)
               $(container).slideUp(300, callback);
            else
               $(container).hide();
            isExpanded = false;
         }

         this.isExpanded = function() {
            return isExpanded;
         }

         this.isActivated = function() {
            return isActivated;
         }
         
         this.createField = function(header, value, editType) {
            editable = (editType == null) ? false : true;
            fields.push(new BodyField(header,
               value, editable, editType));
         }

         this.create = function(isForAdding) {
            managePanel.create(container, isForAdding);
         }

         this.makeEditable = function() {
            isEditable = true;
            for(var field in fields)
               fields[field].makeEditable();
         }

         this.makeStatic = function() {
            isEditable = false;
            for(var field in fields)
               fields[field].makeStatic();
         }

      };

      Body.BODYFIELD_EDIT_TYPE = 
      {
            SIMPLE:  'input',
            COMPLEX: 'textarea',
      }
      
      var container = $(document.createElement('div'))
      .attr('id','showcase-element');
      var header = new Header();
      var body = new Body();
      var data = null;

      this.create = function(elementJSON, isForAdding) {
         
         data = elementJSON;
         switch(tab.getCode()) {

            case ShowcaseTab.CODES.OBJ:
               header.create((isForAdding) ? 'Add New' : elementJSON.objectName);
               body.createField('Name',
                  (isForAdding) ? '' : elementJSON.objectName,
                  (isForAdding) ? Body.BODYFIELD_EDIT_TYPE.SIMPLE : null);
               body.createField('Class', 
                  (isForAdding) ? '' : elementJSON.classPOJO.name,
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               break;

            case ShowcaseTab.CODES.CLS:
               header.create((isForAdding) ? 'Add new' : elementJSON.name);
               body.createField('Name', 
                  (isForAdding) ? '' : elementJSON.name,
                  (isForAdding) ? Body.BODYFIELD_EDIT_TYPE.SIMPLE : null);
               body.createField('Description', 
                  (isForAdding) ? '' : elementJSON.description,
                  Body.BODYFIELD_EDIT_TYPE.COMPLEX);
               break;

            case ShowcaseTab.CODES.STL:
               header.create((isForAdding) ? 'Add new' : elementJSON.name);
               body.createField('Name', 
                  (isForAdding) ? '' : elementJSON.name,
                  (isForAdding) ? Body.BODYFIELD_EDIT_TYPE.SIMPLE : null);
               body.createField('Family',
                  (isForAdding) ? '' : elementJSON.familyPOJO.name,
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               body.createField('Is Mandatory',
                  (isForAdding) ? '' : ((elementJSON.mandatory) ? 'true' : 'false'),
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               body.createField('Is Multiple',
                  (isForAdding) ? '' : ((elementJSON.multiple) ? 'true' : 'false'),
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               break;

            case ShowcaseTab.CODES.FAM:
               header.create((isForAdding) ? 'Add new' : elementJSON.name);
               body.createField('Name',
                  (isForAdding) ? '' : elementJSON.name,
                  (isForAdding) ? Body.BODYFIELD_EDIT_TYPE.SIMPLE : null);
               body.createField('Description',
                  (isForAdding) ? '' : elementJSON.description,
                  Body.BODYFIELD_EDIT_TYPE.COMPLEX);
               break;

            case ShowcaseTab.CODES.CSA:
               header.create((isForAdding) ? 'Add new' : (elementJSON.classPOJO.name + 
                  ' - ' + elementJSON.stylePOJO.name));
               body.createField('Class',
                  (isForAdding) ? '' : elementJSON.classPOJO.name,
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               body.createField('Style',
                  (isForAdding) ? '' : elementJSON.stylePOJO.name,
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               break;

            case ShowcaseTab.CODES.OBV:
               header.create((isForAdding) ? 'Add new' : (elementJSON.objectPOJO.objectName + 
                  ' - ' + elementJSON.stylePOJO.name));
               body.createField('Object',
                  (isForAdding) ? '' : elementJSON.objectPOJO.objectName,
                  (isForAdding) ? Body.BODYFIELD_EDIT_TYPE.SIMPLE : null);
               body.createField('Style',
                  (isForAdding) ? '' : elementJSON.stylePOJO.name,
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               body.createField('Value',
                  (isForAdding) ? '' : elementJSON.value,
                  Body.BODYFIELD_EDIT_TYPE.SIMPLE);
               break;

         }
         body.create(isForAdding);
         header.appendTo(container);
         body.appendTo(container);   
         if(isForAdding)
            body.makeEditable();     
      }

      this.appendTo = function(showcaseContainer) {
         $(showcaseContainer).append(container);
         body.hide(false);
      }

      this.remove = function() {
         $(container).remove();
      }

   };

   /* Function addMouseHoverEvents */
   function addMouseHoverEvents(DOMObject, checkBlockState) {

      $(DOMObject)
      .mouseover(function() {
         if(!checkBlockState || (checkBlockState && !$(this).hasClass('blocked'))) {
            $(this).addClass('hover'); 
         }
      })
      .mouseout(function() {
         $(this).removeClass('hover');
      });

   };


   var page = 0;
   var maxPage = 0;
   var tab = null;
   var container = $('#showcase-elements');
   var informer = new ShowcaseInformer();
   var searchMenu = new ShowcaseSearch();
   var elements = new Array();
   var newElement = null;
   var showcase = this;

   var tabs = 
   {
      'obj' : new ShowcaseTab(ShowcaseTab.CODES.OBJ),
      'cls' : new ShowcaseTab(ShowcaseTab.CODES.CLS),
      'stl' : new ShowcaseTab(ShowcaseTab.CODES.STL),
      'fam' : new ShowcaseTab(ShowcaseTab.CODES.FAM),
      'csa' : new ShowcaseTab(ShowcaseTab.CODES.CSA),
      'obv' : new ShowcaseTab(ShowcaseTab.CODES.OBV),       
   };

   var buttons = 
   {
      'sch' : new ShowcaseButton(ShowcaseButton.CODES.SCH),
      'add' : new ShowcaseButton(ShowcaseButton.CODES.ADD),
      'up'  : new ShowcaseButton(ShowcaseButton.CODES.UP),
      'dwn' : new ShowcaseButton(ShowcaseButton.CODES.DWN),
      'fst' : new ShowcaseButton(ShowcaseButton.CODES.FST),
      'lst' : new ShowcaseButton(ShowcaseButton.CODES.LST),
   }

   this.makePage = function(msg) {

      var self = this;
      informer.clear(function() {

         self.clear();
         if(msg.errorCode != Application.ERROR_TYPE.ERR_NONE.NAME) {   
            informer.warning(Application.ERROR_TYPE[msg.errorCode].MESSAGE, function() {
               msg.errorCode = Application.ERROR_TYPE.ERR_NONE.NAME;
               msg.manageType = '';
               self.makePage(msg);
            });
            return;
         }
         else if(msg.manageType == Application.MANAGE_TYPE.SEARCH.toUpperCase()) {
            self.clearMenu();
         }
         else {
            page = msg.currentPage;
            maxPage = msg.pagesAmount;
            if(page == 0) {
               buttons[ShowcaseButton.CODES.UP].block();
               buttons[ShowcaseButton.CODES.FST].block();
            }
            else {
               buttons[ShowcaseButton.CODES.UP].unblock();
               buttons[ShowcaseButton.CODES.FST].unblock();
            }
            if(page == maxPage) {
               buttons[ShowcaseButton.CODES.DWN].block();
               buttons[ShowcaseButton.CODES.LST].block();
            }
            else {
               buttons[ShowcaseButton.CODES.DWN].unblock();
               buttons[ShowcaseButton.CODES.LST].unblock();
            }
            showcase.configureButtons(tab.getCode());
            searchMenu.create(tab.getCode());
            tab.select();
         }
         
         var element = null;
         for(var pojo in msg.pojos) {
            element = new ShowcaseElement();
            element.create(msg.pojos[pojo], false);
            element.appendTo(container);
            elements.push(element);
         }
         self.unblockMenu();
      });
   }

   this.clear = function() {
      searchMenu.hide();
      container.empty();
      elements.length = 0;
      
   }

   this.getPage = function() {
      return page;
   }

   this.getTab = function() {
      return tab;
   }

   this.init = function() {

      tabs[ShowcaseTab.CODES.OBJ].click();
      buttons[ShowcaseButton.CODES.UP].click(function() {
         if(page > 0 && !buttons[ShowcaseButton.CODES.UP].isBlocked()) {
            informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
               showcase.deactivateButtons();
               showcase.blockMenu();
               showcase.clear();
               page--;
               app.load();
            });
         }
      });
      buttons[ShowcaseButton.CODES.DWN].click(function() {
         if(page < maxPage && !buttons[ShowcaseButton.CODES.DWN].isBlocked()) {
            informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
               showcase.deactivateButtons();
               showcase.blockMenu();
               showcase.clear();
               page++;
               app.load();
            });
         }
      });
      buttons[ShowcaseButton.CODES.FST].click(function() {
         if(page != 0 && !buttons[ShowcaseButton.CODES.FST].isBlocked()) {
            informer.info(ShowcaseInformer.MESSAGES.WAIT , function() {
               showcase.deactivateButtons();
               showcase.blockMenu();
               showcase.clear();
               page = 0;
               app.load();
            });
         }
      });
      buttons[ShowcaseButton.CODES.LST].click(function() {
         if(page != maxPage && !buttons[ShowcaseButton.CODES.LST].isBlocked()) {
            informer.info(ShowcaseInformer.MESSAGES.WAIT, function() {
               showcase.deactivateButtons();
               showcase.blockMenu();
               showcase.clear();
               page = maxPage;
               app.load();
            });
         }
      });
      buttons[ShowcaseButton.CODES.ADD].click(function() {
         if(!buttons[ShowcaseButton.CODES.ADD].isBlocked()) {
            buttons[ShowcaseButton.CODES.ADD].block();
            newElement = new ShowcaseElement();
            newElement.create(null, true);
            newElement.appendTo(container);
         }
      });
      buttons[ShowcaseButton.CODES.SCH].click(function () {
         if(!buttons[ShowcaseButton.CODES.SCH].isBlocked()) {
            if(!searchMenu.isVisible())
               searchMenu.show();
            else
               searchMenu.hide();
         }
      });
   }

   this.configureButtons = function(tabCode) {

      switch(tabCode) {

         case ShowcaseTab.CODES.OBJ:
            buttons[ShowcaseButton.CODES.SCH].unblock();
            buttons[ShowcaseButton.CODES.ADD].unblock();
            break;

         case ShowcaseTab.CODES.CLS:
            buttons[ShowcaseButton.CODES.ADD].unblock();
            break;
         
         case ShowcaseTab.CODES.STL:
            buttons[ShowcaseButton.CODES.ADD].unblock();
            break;

         case ShowcaseTab.CODES.FAM:
            buttons[ShowcaseButton.CODES.ADD].unblock();
            break;

         case ShowcaseTab.CODES.CSA:
            buttons[ShowcaseButton.CODES.ADD].unblock();
            break;
         
         case ShowcaseTab.CODES.OBV:
            buttons[ShowcaseButton.CODES.SCH].unblock();
            buttons[ShowcaseButton.CODES.ADD].unblock();
            break;
   
      }

   }

   this.activateButtons = function() {
      for(var button in buttons)
         buttons[button].unblock();
   }

   this.deactivateButtons = function() {
      for(var button in buttons)
         buttons[button].block();
   }

   this.clearMenu = function() {
      for(var tab in tabs)
         tabs[tab].deselect();
   }

   this.blockMenu = function() {
      for(var tab in tabs)
         tabs[tab].block();
   }

   this.unblockMenu = function() {
      for(var tab in tabs)
         tabs[tab].unblock();
   }

   this.removeAdded = function() {
      newElement.remove();
      newElement = null;
      buttons[ShowcaseButton.CODES.ADD].unblock();
   }

   this.error = function() {
      informer.warning(ShowcaseInformer.MESSAGES.ERROR, null);
      self.unblockMenu();
      self.clearMenu();
   }

};



















