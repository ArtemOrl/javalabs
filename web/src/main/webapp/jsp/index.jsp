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
      		<div id="menu">
      			<div id="obj" class="tab selected">Objects</div>
      			<div id="cls" class="tab">Classes</div>
      			<div id="fam" class="tab">Families</div>
      			<div id="stl" class="tab">Styles</div>
      		</div>
      		<div id="navigation">
      			<div id="nav-search">
      				<form id="nav-search-form">
      					<input id="nav-search-param-field" name="search-param" type="text" size="100">
      					<div id="nav-search-button">Search</div>
      				</form>
      			</div>
      			<div id="nav-body">
      				<div id="nav-body-content">
	      				<div id="nav-body-message">
	      				</div>
	      				<div id="nav-body-elems">
	      				</div>
      				</div>
      				<div id="nav-body-buttons">
      					<div id="nav-body-scroll">
	      					<div id="nav-body-scroll-up" class="nav-body-scroll">Up</div>
	      					<div id="nav-body-scroll-down" class="nav-body-scroll">Down</div>
	      				</div>
	      				<div id="nav-body-add-button">Add new</div>
      				</div>
      				<div id="nav-body-add-form">
      				</div>
      			</div>
      		</div>
      	</div>
      </div>
   </body>
</html>
