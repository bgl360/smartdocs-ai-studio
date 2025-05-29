from inference import Inference
from result_parser import ResultParser
import os
import argparse


# This is a sample code to show how to use the library


def main(file_path: str) -> None:
    # enter the api key and project id
    api_key = ""
    project_id = ""
    # you can also read api_key from environment variable
    # api_key = os.getenv('API_KEY')

    client = Inference(project_id=project_id, api_key=api_key)

    file_id = client.upload_single_file(file_path,
                                        is_async="true",
                                        is_training="false",
                                        upload_user="test@bglcorp.com.au",
                                        upload_source="localTest",
                                        third_party_group_1="firmId",
                                        third_party_group_2="fundId",
                                        third_party_id="123",
                                        label_ids="12345678",
                                        label_names="test")

    if file_id:
        if client.inference_readiness_status_polling(file_id):
            output_file_path = client.download_result(file_id, output_folder="results")
            print(f"Result downloaded: {output_file_path}")
            # result parsing
            result_parser = ResultParser(output_file_path)
            business_models = result_parser.business_models_mapping()
            page_annotations = result_parser.page_annotations_mapping()
            word_annotations = result_parser.word_annotations_mapping()
            object_annotations = result_parser.object_annotations_mapping()
            print(f"Business Models: {business_models}")
            print(f"Page Annotations: {page_annotations}")
            print(f"Word Annotations: {word_annotations}")
            print(f"Object Annotations: {object_annotations}")
        else:
            print("File processing time out")
    else:
        print("File upload failed")


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '--file',
        default=os.path.abspath("resources/samples/extraction/insurance-receipt-sample.pdf"),
        help='path to the input file (default: %(default)s)')
    args = parser.parse_args()
    main(args.file)
