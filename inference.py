import traceback

import requests
import time
import os
import json


class Inference:
    # Define class attributes
    BASE_URL_CONSTANT = "https://smartdocs-web.doc.com.ai/api/v1"

    def __init__(self, project_id: str, api_key: str, base_url: str = BASE_URL_CONSTANT):
        self.base_url = base_url
        self.project_id = project_id
        self.api_key = api_key
        self.headers = {"api-key": f"{api_key}"}

    # This is a private method
    def _validate_pdf_file(self, file_name: str) -> bool:
        # return true if the extension of file is pdf otherwise false
        # split the filename into the base name and the extension
        base_name, extension = os.path.splitext(file_name)
        if extension == ".pdf":
            return True
        else:
            return False

    def upload_single_file(self,
                           file_path: str,
                           is_async: str = "true",
                           is_training: str = "false",
                           upload_user: str = "smartdocs@bglcorp.com.au",
                           upload_source: str = "localTest",
                           third_party_group_1: str = "firmId",
                           third_party_group_2: str = "fundId",
                           third_party_id: str = "123",
                           label_ids: str = "12345678",
                           label_names: str = "test"
                           ) -> str:

        query_params = {
            "isAsync": is_async,
            "isTraining": is_training,
            "uploadUser": upload_user,
            "uploadSource": upload_source,
            "thirdPartyGroup1": third_party_group_1,
            "thirdPartyGroup2": third_party_group_2,
            "thirdPartyId": third_party_id,
            "labelIds": label_ids,
            "labelNames": label_names
        }

        query_string = "&".join([f"{key}={value}" for key, value in query_params.items()])
        url = f"{self.base_url}/workflows/upload?projectId={self.project_id}&{query_string}"
        with open(file_path, "rb") as pdf_file:
            file_name = os.path.basename(file_path)
            # check if the file is a pdf file
            if self._validate_pdf_file(file_name):
                response = requests.post(url, headers=self.headers,
                                         files={"user_file": (file_name, pdf_file, "application/pdf")})
                if response.status_code == 200:
                    file_id = response.json()['fileId']
                    print(f"file uploaded successfully with fileId: {file_id}")
                    return file_id
                else:
                    try:
                        response.raise_for_status()
                    except requests.exceptions.HTTPError as e:
                        print(f"An error occurred while making the request: {e}")
                        print(f"Detailed error message: {response.text}")
                        traceback.print_exc()
            else:
                raise ValueError(f"File {file_name} is not a PDF file")

    def check_prediction_status(self, fild_id: str) -> str:

        url = f"{self.base_url}/files/{fild_id}?projectId={self.project_id}"
        response = requests.get(url, headers=self.headers)
        if response.status_code == 200:
            return response.json()["workflowStatus"]
        else:
            try:
                response.raise_for_status()
            except requests.exceptions.HTTPError as e:
                print(f"An error occurred while making the request: {e}")
                print(f"Detailed error message: {response.text}")
                traceback.print_exc()

    # 5 * 60 = 300 seconds = 5 minutes
    def inference_readiness_status_polling(self, file_id: str, poll_interval: int = 5, max_attempts: int = 60) -> bool:

        attempts = 0
        while attempts < max_attempts:
            status = self.check_prediction_status(file_id)
            if status == "RESULT_READY":
                return True
            print("Result is not ready, waiting for inference readiness...")
            time.sleep(poll_interval)
            attempts += 1
        return False

    def download_result(self, file_id: str, output_folder: str = "results") -> str:

        url = f"{self.base_url}/files/{file_id}/result?projectId={self.project_id}"
        response = requests.get(url, headers=self.headers)
        if response.status_code == 200:
            download_url = response.json()["url"]
            result_response = requests.get(download_url)

            if result_response.status_code == 200:
                os.makedirs(output_folder, exist_ok=True)
                output_file_path = os.path.join(output_folder, f"{file_id}.json")

                with open(output_file_path, "w") as output_file:
                    # to json string
                    json_data = json.loads(result_response.content.decode("utf-8"))
                    json.dump(json_data, output_file)
                return output_file_path
            else:
                try:
                    result_response.raise_for_status()
                except requests.exceptions.HTTPError as e:
                    print(f"An error occurred while making the request: {e}")
                    print(f"Detailed error message: {result_response.text}")
                    traceback.print_exc()
        else:
            try:
                response.raise_for_status()
            except requests.exceptions.HTTPError as e:
                print(f"An error occurred while making the request: {e}")
                print(f"Detailed error message: {response.text}")
                traceback.print_exc()
