# sdd-api-python-client

For Smart Docs 360 Developer

### Installation

Firstly, we install pythond >=3.7. Then run the following commands to create a new virtual environment and install all dependencies under requirements.txt

```sh
# Create a new virtual environment.
python -m venv .venv
# Activate the virtual environment.
source .venv/bin/activate
# Install all the dependencies. 
pip install -r requirements.txt
```

### Quick Start

main.py is a temple showing how to retrieve the extraction/classification by uploading a single pdf file.
Prior to execution, please make sure you update the main.py with api key and project id.

```sh
python main.py --file <path to pdf file>
```

### AI Assistant Integration

> Visit below link for more details on how to integrate AI Assistant into your web application.
https://smartdocs-ai-studio.readme.io/docs/chat-bot-integration#/

if no file path is provided, the default file path is "resources/samples/extraction/insurance-receipt-sample.pdf" and
you need to have a project with project type "Insurance Receipt" created in your account to test out the extraction.
