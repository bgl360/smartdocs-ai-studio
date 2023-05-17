# sdd-api-python-client
For Smart Docs 360 Developer

### Installation
install pythond >=3.7 and then install all dependencies under requirements.txt
```sh
pip install -r requirements.txt
```

### Quick Start
main.py is a temple showing how to retrieve the extraction/classification by uploading a single pdf file.
Prior to execution, please make sure you update the main.py with api key and project id.

```sh
python main.py -f <path to pdf file>
```
if no file path is provided, the default file path is "resources/samples/extraction/insurance-receipt-sample.pdf" and 
you need to have a project with project type "Insurance Receipt" created in your account to test out the extraction.