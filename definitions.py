from typing import Dict, List, Union

AnnotationItem = Dict[str, str]
PageAnnotationData = Dict[str, Union[str, List[AnnotationItem]]]
PageAnnotationsList = List[PageAnnotationData]