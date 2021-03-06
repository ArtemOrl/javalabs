<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
   <head>
   	<title>Catalogue of object properties</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <link href="/css/style.css" rel="stylesheet" type="text/css"/>
      <link href='http://fonts.googleapis.com/css?family=Fredericka+the+Great' rel='stylesheet' type='text/css'>
      <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro' rel='stylesheet' type='text/css'>
      <script src="/js/jquery-1.7.2.min.js" type="text/javascript"></script>
      <script src="/js/index.js" type="text/javascript"></script>
      <style>height:100%</style>
   </head>
   <body>
      <div id="main">
      	<div id="head">
      		Catalogue Of Object Properties
      	</div>
      	<div id="body">
      		<div id="showcase-menu">
      			<div id="showcase-tab-obj" class="tab">Objects</div>
               <div id="showcase-tab-obv" class="tab">Values</div>
      			<div id="showcase-tab-cls" class="tab">Classes</div>
      			<div id="showcase-tab-stl" class="tab">Styles</div>
               <div id="showcase-tab-csa" class="tab">Class-Style Associations</div>
               <div id="showcase-tab-fam" class="tab">Families</div>
      		</div>
      		<div id="showcase">
               <div id="showcase-informer"></div>
               <div id="showcase-elements"></div>
      			<div id="showcase-manage-panel">
      					<div id="showcase-manage-panel-scrolls">
	      					<div id="showcase-button-up"  class="button">Up</div>
	      					<div id="showcase-button-dwn" class="button">Down</div>
                        <div id="showcase-button-fst" class="button">First</div>
                        <div id="showcase-button-lst" class="button">Last</div>
	      				</div>
	      				<div id="showcase-button-add" class="button">Add new</div>
                     <div id="showcase-button-sch" class="button">Search Menu</div>
      				</div>
      			</div>
               <div id="showcase-forms">
                  <div id="showcase-form-sch">
                     <select id="showcase-form-sch-type"></select>
                     <input id="showcase-form-sch-input">
                     <div id="showcase-form-sch-button">Search</div>
                  </div>
               </div>
      		</div>
      	</div>
      </div>
   </body>
</html>
