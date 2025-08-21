<%@ include file="/init.jsp"%>
<!-- Button trigger modal -->
<p>
	<b><liferay-ui:message key="employee.caption" /></b>
</p>

<button type="button" class="btn btn-primary" data-toggle="modal"
	data-target="#exampleModal">Launch demo modal</button>


<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog " role="document">
		<div class="container m-5">
			<div class="card" style="width: 508px; height: 219px;">
				<div class="card-body  ">
					<h1 class="">Are you sure want to to delete this employee ?</h1>
					<div class="d-flex flex-row justify-content-end mt-6">
						<a href="#" class="btn delete-model-no-button">No</a> <a href="#"
							class="btn delete-model-yes-button ml-3">Yes</a>
					</div>
				</div>
			</div>

		</div>

	</div>
</div>

<style>
.delete-model-yes-button {
	background-color: #00979E;
	color: white;
	width: 122px;
}

.delete-model-no-button {
	background-color: white;
	color: black;
	width: 122px;
	border: 1px solid black;
}
</style>