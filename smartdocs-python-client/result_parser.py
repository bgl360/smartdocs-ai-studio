import json
import os
from typing import List, Dict, Any
from definitions import PageAnnotationsList


class ResultParser:

    def __init__(self, file_path: str):
        self.file_path = file_path
        if ResultParser._is_json(file_path):
            with open(self.file_path, "r") as json_file:
                self.inference_data = json.load(json_file)
                self.word_id_to_text = {block["Id"]: block["Text"] for block in
                                        self.inference_data["Document"]["Blocks"]
                                        if block["BlockType"] == "WORD"}
                self.page_id_to_number = {block['Id']: block['Page'] for block in
                                          self.inference_data["Document"]["Blocks"]
                                          if block["BlockType"] == "PAGE"}
        else:
            raise ValueError(f"Expected a json file but got {self.file_path}")

    @staticmethod
    def _is_json(file_path) -> bool:
        # return true if the extension of file is json otherwise false
        # split the filename into the base name and the extension
        base_name, extension = os.path.splitext(file_path)
        if extension == ".json":
            return True
        else:
            return False

    def word_annotations_mapping(self) -> List[Dict[str, str]]:
        result = []
        if self.inference_data.get("WordAnnotations"):
            for word_annotation in self.inference_data["WordAnnotations"]:
                word_id = word_annotation["WordId"]
                word_annotation["WordText"] = self.word_id_to_text[word_id]
                result.append(word_annotation)
        return result

    def page_annotations_mapping(self) -> PageAnnotationsList:
        result = []
        if self.inference_data.get("PageAnnotations"):
            for page_annotation in self.inference_data["PageAnnotations"]:
                page_id = page_annotation["Page"]
                page_annotation["PageNumber"] = self.page_id_to_number[page_id]
                result.append(page_annotation)
        return result

    # reuse page annotations mapping
    def object_annotations_mapping(self) -> PageAnnotationsList:
        result = []
        if self.inference_data.get("ObjectAnnotations"):
            for object_annotation in self.inference_data["ObjectAnnotations"]:
                page_id = object_annotation["Page"]
                object_annotation["PageNumber"] = self.page_id_to_number[page_id]
                result.append(object_annotation)
        return result

    def business_models_mapping(self) -> List[Dict[str, Any]]:
        result = []
        if self.inference_data.get("BusinessModels"):
            for business_model in self.inference_data["BusinessModels"]:
                result.append(business_model)
        return result
